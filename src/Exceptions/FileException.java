package Exceptions;

public class FileException extends Exception {
    int exceptionCode = 0;
    private String errorMessage;
    private final String EXCEPTION_MESSAGE = " -- File Error: ";


    public FileException() { errorMessage = "UNKNOWN"; }
    public FileException(int exceptionCode) {
        switch (exceptionCode) {
            case 0:
                errorMessage = "UNKNOWN";
            case 1:
                errorMessage = "is a directory";
            case 2:
                errorMessage = "file does not exist";
            case 3:
                errorMessage = "file is NOT a XML file";
            case 4:
                errorMessage = "the XML file isn't match to the schema";
        }
    }

    @Override
    public String getMessage() {
        return (EXCEPTION_MESSAGE + errorMessage + " --");
    }
}
