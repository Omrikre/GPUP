package Engine.Enums;

public enum MissionState {
    PAUSED {
        @Override
        public String toString() {
            return "Paused";
        }
    },
    STOPPED {
        @Override
        public String toString() {
            return "Stopped";
        }
    },
    FINISHED {
        @Override
        public String toString() {
            return "Finished";
        }
    },
    READY {
        @Override
        public String toString() {
            return "Ready";
        }
    }
}
