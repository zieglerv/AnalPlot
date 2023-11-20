/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jlab.clas.analysis.Constants;
import org.jlab.clas.analysis.plots.GenericAnalReader.Target;
import org.jlab.io.base.DataEvent;

/**
 *
 * @author veronique
 */
public class GenericAnalyzer {
    private int _parentPID;
    public GenericAnalReader gar;
    private int _refIdx;
    public GenericAnalyzer(int parentPID, Map<Integer, String> particlePIDs, 
            int refIdx) {
        _parentPID = parentPID; 
        _refIdx = refIdx; 
        gar = new GenericAnalReader(parentPID, particlePIDs); 
        Constants.Load();
    }
    
    public void processEvent(DataEvent event, double beamE, Target tar) {
        gar.readBanks(event, _parentPID, beamE, tar, _refIdx);
    }
    
    public static String getInputFile() {
        String[] options = { "Enter File Path", "Select File" };
        int choice = JOptionPane.showOptionDialog(null, "How do you want to specify the input file?", "Input Method",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // Manually enter the file path
            return JOptionPane.showInputDialog("Enter the input file path:");
        } else if (choice == 1) {
            // Use a file chooser dialog to select the file
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                return selectedFile.getAbsolutePath();
            }
        }

        return null; // Return null if no file is selected or entered.
    }
    
    public static String[] findFiles(String userInput) {
        List<String> foundFiles = new ArrayList<>();
        try {
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + userInput);
            Files.walk(Paths.get("."))
                    .filter(path -> pathMatcher.matches(path.getFileName()))
                    .forEach(path -> foundFiles.add(path.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundFiles.toArray(new String[0]);
    }

    private static void displayFilePaths(String[] filePaths) {
        for (String path : filePaths) {
            System.out.println(path);
        }
    }
    
}
