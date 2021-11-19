package Engine.Enums;

public enum State {
    FROZEN {
        @Override
        public String toString() {
            return "frozen";
        }
    },
    SKIPPED, WAITING, IN_PROCESS,
    FINISHED_SUCCESS{
        @Override
        public String toString() {
            return "success";
        }
    }, FINISHED_FAILURE{
        @Override
        public String toString() {
            return "failure";
        }
    }, FINISHED_WARNINGS{
        @Override
        public String toString() {
            return "success with warnings";
        }
    };


   @Override
    public String toString() {

        String s = super.toString().substring(0, 1).toUpperCase() +
                super.toString().substring(1).toLowerCase();
        if (s.contains("_"))
            return s.replace('_', ' ');
        return s;
    }

}
