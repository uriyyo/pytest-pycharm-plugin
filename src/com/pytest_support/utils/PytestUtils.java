package com.pytest_support.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.python.psi.PyFile;
import com.pytest_support.consts.PytestConsts;

public class PytestUtils {

    public static boolean isPytestFile(PyFile file) {
        return PytestConsts.PYTEST_FILES.contains(file.getName());
    }

    public static boolean isTestFile(PyFile file) {
        return file.getName().startsWith("test_");
    }

    public static boolean isTestOrPytestFile(PyFile file) {
        return isPytestFile(file) || isTestFile(file);
    }

    public static boolean isTestOrPytestFile(PsiElement psiElement) {
        PsiFile originalFile = psiElement.getContainingFile().getOriginalFile();

        if (!(originalFile instanceof PyFile)) {
            return false;
        }

        return isTestOrPytestFile((PyFile) originalFile);
    }
}

