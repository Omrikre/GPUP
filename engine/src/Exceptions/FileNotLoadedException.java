package Exceptions;

public class FileNotLoadedException extends Exception{
    @Override
    public String getMessage() {
        return "-- No file is loaded, Please load file before select this option -- ";
    }
}
