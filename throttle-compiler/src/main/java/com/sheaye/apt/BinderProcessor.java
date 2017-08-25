package com.sheaye.apt;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import throttle.BindClick;

@AutoService(Processor.class)
public class BinderProcessor extends AbstractProcessor {

    // 日志工具
    private Messager mLogUtil;
    // Element操作工具
    private Elements mElementUtils;
    // Binder类生成工具集，一个类对应一个ClassHolder，例如MainActivity对应MainActivity$$Binder的ClassHolder
    private HashMap<String, ClassHolder> mClassHolderMap;
    // 生成文件的工具
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mLogUtil = processingEnvironment.getMessager();// 获取日志工具
        mElementUtils = processingEnvironment.getElementUtils();// 获取Element操作工具
        mFiler = processingEnvironment.getFiler();// 获取生成文件的工具
        mClassHolderMap = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mClassHolderMap.clear();
        parseBindClick(roundEnvironment);
        for (String key : mClassHolderMap.keySet()) {
            generateFile(mClassHolderMap.get(key));
        }
        return false;
    }

    private void parseBindClick(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindClick.class)) {

            // 包
            PackageElement packageElement = mElementUtils.getPackageOf(element);// 获取包元素
            String packageName = packageElement.getQualifiedName().toString();

            // 类
            TypeElement classElement = (TypeElement) element.getEnclosingElement();// 获取所属的类元素
            String targetFullClassName = classElement.getQualifiedName().toString();// 获取类的全名
            String targetClassName = targetFullClassName.substring(packageName.length() + 1);// 获取简单类名

            // 方法
            ExecutableElement methodElement = (ExecutableElement) element;// 强转为方法元素
            String methodName = methodElement.getSimpleName().toString();// 获取方法名

            int[] values = methodElement.getAnnotation(BindClick.class).value();// 获取BindClick的value值
            logNote("targetFullClassName: " + targetFullClassName + ", methodName: " + methodName + ", viewIds:" + values.toString());

            BindClickMethod method = new BindClickMethod(values, methodName, getMethodParameterTypes(methodElement));
            ClassHolder classHolder = mClassHolderMap.get(targetFullClassName);// 一个类对应一个ClassHolder
            if (classHolder != null) {// 如果已经有存在的ClassHolder，直接添加方法
                classHolder.setBindClickMethod(method);
            } else {// 否则创建ClassHolder,添加方法，加入map
                classHolder = new ClassHolder(packageName, targetClassName);
                classHolder.setTypeElement(classElement);
                classHolder.setBindClickMethod(method);
                mClassHolderMap.put(targetFullClassName, classHolder);
            }
        }
    }

    // 获取参数类型列表
    private List<String> getMethodParameterTypes(ExecutableElement methodElement) {
        List<? extends VariableElement> parameterElements = methodElement.getParameters();// 获取方法的参数元素集
        if (parameterElements.size() == 0) {
            return null;
        }
        List<String> parameterTypes = new ArrayList<>();
        for (VariableElement parameterElement : parameterElements) {
            TypeMirror parameterType = parameterElement.asType();
            if (parameterType instanceof TypeVariable) {
                parameterType = ((TypeVariable) parameterType).getUpperBound();
            }
            parameterTypes.add(parameterType.toString());
        }
        return parameterTypes;
    }

    private void generateFile(ClassHolder classHolder) {
        try {
            JavaFileObject jfo = mFiler.createSourceFile(classHolder.getClassFullName(), classHolder.getTypeElement());
            Writer writer = jfo.openWriter();
            writer.write(classHolder.generateJavaCode());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    private void logNote(String message) {
        mLogUtil.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void logError(String message) {
        mLogUtil.printMessage(Diagnostic.Kind.ERROR, message);
    }

    // 本处理器处理的注解类型集合
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        // getCanonicalName()获取包括报名在内的完整类名
        annotationTypes.add(BindClick.class.getCanonicalName());
        return annotationTypes;
    }

    // 所支持的Java版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
