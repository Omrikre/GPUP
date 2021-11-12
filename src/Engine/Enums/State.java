package Engine.Enums;

public enum State {
    FROZEN, SKIPPED, WAITING, IN_PROCESS, FINISHED_SUCCESS, FINISHED_FAILURE, FINISHED_WARNINGS;


    @Override
    public String toString() {
        return super.toString().substring(0, 1).toUpperCase() +
                super.toString().substring(1).toLowerCase();
    }

}
