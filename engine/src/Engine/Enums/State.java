package Engine.Enums;

import java.io.Serializable;

public enum State {
    FROZEN {
        @Override
        public String toString() {
            return "frozen";
        }
    },
    SKIPPED, WAITING,
    IN_PROCESS{
        @Override
        public String toString() {
            return "in process";
        }
    },
    FINISHED_SUCCESS {
        @Override
        public String toString() {
            return "success";
        }
    }, FINISHED_FAILURE {
        @Override
        public String toString() {
            return "failure";
        }
    }, FINISHED_WARNINGS {
        @Override
        public String toString() {
            return "success with warnings";
        }
    }

}
