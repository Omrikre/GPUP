package components.graphManager.info.generalInfo;

public class targetsInfoBox {
    private String type;
    private int quantity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public targetsInfoBox(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }
}
