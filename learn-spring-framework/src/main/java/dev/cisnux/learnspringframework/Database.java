package dev.cisnux.learnspringframework;

public final class Database {
    private static Database INSTANCE;

    private Database() {
    }

    public static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }
}
