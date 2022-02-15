package components.graphManager.info.generalInfo;

public class serialSetBox {
    private String setName;
    private int membersNum;
    private String membersNames;

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getMembersNum() {
        return membersNum;
    }

    public void setMembersNum(int membersNum) {
        this.membersNum = membersNum;
    }

    public String getMembersNames() {
        return membersNames;
    }

    public void setMembersNames(String membersNames) {
        this.membersNames = membersNames;
    }

    public serialSetBox(String setName, int membersNum, String membersNames) {
        this.setName = setName;
        this.membersNum = membersNum;
        this.membersNames = membersNames;
    }
}


