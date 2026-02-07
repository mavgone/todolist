class Task {
    private String description;
    private boolean isComplete;
    private int id;

    public Task(String description, boolean isComplete, int id) {
        this.description = description;
        this.isComplete = isComplete;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ". " + description + " || " +
                (isComplete ? "Выполнена" : "Не выполнена");
    }
}
