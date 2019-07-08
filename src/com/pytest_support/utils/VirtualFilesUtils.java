package com.pytest_support.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.PsiManager;
import com.jetbrains.python.psi.PyFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class VirtualFilesUtils {

    public static Collection<VirtualFile> getAllChildes(VirtualFile virtualFile) {
        ArrayList<VirtualFile> files = new ArrayList<>(Arrays.asList(virtualFile.getChildren()));
        VfsUtilCore.visitChildrenRecursively(virtualFile, new VirtualFileVisitor<Object>() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                files.add(file);
                return true;
            }
        });

        return files;
    }


    public static Stream<PyFile> allPyFileStream(Project project) {
        return Arrays.stream(project.getBaseDir().getChildren())
                .flatMap(virtualFile -> VirtualFilesUtils.getAllChildes(virtualFile).stream())
                .map(PsiManager.getInstance(project)::findFile)
                .filter(file -> file instanceof PyFile)
                .map(file -> (PyFile) file);
    }
}
