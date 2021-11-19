package Exceptions;

import Engine.Enums.Bond;
import Engine.Graph;

import java.io.File;

public class FileException extends Exception {
    private String errorMessage = "UNKNOWN";


    public FileException() {

    }

    public FileException(int exceptionCode, String t) {
        switch (exceptionCode) {
            case 1:
                errorMessage = "the file: " + t + " is not an XML file";
                break;
            case 2:
                errorMessage = "the target's name: " + t + " isn't unique";
                break;
        }
    }

    public FileException(int exceptionCode, String t, Bond bond, String k) {
        switch (exceptionCode) {
            case 3:
                errorMessage = "the target " + t + " does not exist in the graph, yet " + bond + " target " + k;
                break;
            case 4:
                errorMessage = "the target " + t + " is listed as " + bond + " target " + k +
                        ", but the target " + k + " is also listed as " + bond + " target " + k;
                break;
        }
    }

    @Override
    public String getMessage() {
        return (" -- File Error: " + errorMessage + " --");
    }
}
