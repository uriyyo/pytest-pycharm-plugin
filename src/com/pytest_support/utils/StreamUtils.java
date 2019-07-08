package com.pytest_support.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.jetbrains.python.psi.PyFile;
import com.jetbrains.python.psi.PyFunction;
import com.pytest_support.consts.PytestConsts;

import java.util.*;
import java.util.stream.Stream;

public class StreamUtils {

    public static Collection<PyFile> pytestConftests(PsiDirectory directory) {
        if (directory == null) {
            return Collections.emptyList();
        } else if (directory.getProject().getBaseDir().equals(directory.getVirtualFile())) {
            return Collections.emptyList();
        }

        Collection<PyFile> files = new ArrayList<>();
        Optional.ofNullable(directory.findFile(PytestConsts.CONFTEST)).ifPresent(file -> files.add((PyFile) file));
        Optional.ofNullable(directory.getParentDirectory())
                .ifPresent(parentDirectory -> files.addAll(pytestConftests(parentDirectory)));

        return files;
    }

    public static VirtualFile getTestRoot(PsiDirectory directory) {
        Project project = directory.getProject();
        VirtualFile virtualFile = directory.getVirtualFile();

        while (!Objects.equals(virtualFile.getParent(), project.getBaseDir())) {
            virtualFile = virtualFile.getParent();
            if (virtualFile == null) {
                return null;
            }
        }

        return virtualFile;
    }

    public static Stream<PyFunction> pyFixturesStream(PyFile pyFile) {
        Stream<PyFunction> fileFunctions = pyFile.getTopLevelFunctions().stream();
        Stream<PyFunction> conftestFunctions = pytestConftests(pyFile.getContainingDirectory()).stream()
                .flatMap(file -> file.getTopLevelFunctions().stream());

        VirtualFile root = getTestRoot(pyFile.getContainingDirectory());
        String rootPath = root == null ? "" : root.getPath();
        Stream<PyFunction> pyFunctionStream = VirtualFilesUtils
                .allPyFileStream(pyFile.getProject())
                .filter(file -> !file.getVirtualFile().getPath().startsWith(rootPath))
                .flatMap(file -> file.getTopLevelFunctions().stream());

        return Stream.of(fileFunctions, conftestFunctions, pyFunctionStream).flatMap(f -> f);
    }
}
