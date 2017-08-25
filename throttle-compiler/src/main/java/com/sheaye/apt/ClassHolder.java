package com.sheaye.apt;
import java.util.Arrays;

import javax.lang.model.element.TypeElement;
import static throttle.Constant.SUFFIX;

/**
 * Created by yexinyan on 2017/5/24.
 */

public class ClassHolder {

    private String mPackageName;
    private String mTargetClassName;
    private String mSimpleClassName;
    private TypeElement mTypeElement;
    private String mClassFullName;
    private BindClickMethod mBindClickMethod;


    ClassHolder(String packageName, String targetClassName) {
        mPackageName = packageName;
        mTargetClassName = targetClassName;
        mSimpleClassName = targetClassName.replace(".", "$") + SUFFIX;
        mClassFullName = packageName + "." + mSimpleClassName;
    }

    void setBindClickMethod(BindClickMethod method) {
        mBindClickMethod = method;
    }

    void setTypeElement(TypeElement mTypeElement) {
        this.mTypeElement = mTypeElement;
    }

    String getClassFullName() {
        return mClassFullName;
    }

    TypeElement getTypeElement() {
        return mTypeElement;
    }

    String generateJavaCode() {
        int[] resIds = mBindClickMethod.getResIds();
        return "// Generated code from AnticlutterClickBinder. Do not modify!\n" +
                "package " + mPackageName + ";\n" +
                "\n" +
                "import android.view.View;\n" +
                "import com.sheaye.throttle.IBinder;\n" +
                "import com.sheaye.throttle.Finder;\n" +
                "import com.sheaye.throttle.ClickThrottle;\n" +
                "\n" +
                "public class " + mSimpleClassName + "<T extends " + mTargetClassName + "> implements IBinder<T> {\n" +
                "    @Override\n" +
                "    public void bind(final T target, final Object source, final Finder finder) {\n" +
                "        int[] resIds = " + Arrays.toString(resIds).replace("[", "{").replace("]", "}") + ";\n" +
                "        for (int i = 0; i < resIds.length; i++) {\n" +
                "            View view = finder.findViewById(source, resIds[i]);\n" +
                "            if (view != null) {\n" +
                "                view.setOnClickListener(new View.OnClickListener() {\n" +
                "                    @Override\n" +
                "                    public void onClick(View v) {\n" +
                "                        ClickThrottle.checkClickEvent(\"" + mTargetClassName + "\", source, v, new ClickThrottle.ClickExecutor() {\n" +
                "                            @Override\n" +
                "                            public void execute(View view) {\n" +
                "                                target." + mBindClickMethod.getMethodName() + "(view);\n" +
                "                            }\n" +
                "                        });\n" +
                "                    }\n" +
                "                });\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
}
