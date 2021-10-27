package App;

public class Entity {
    String name;
    int level;
    int maxHealth;
    int currentHealth;
    int baseAttack;
    int baseDefense;

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public Entity(String name, int level, int maxHealth, int currentHealth, int baseAttack, int baseDefense) {
        this.name = name;
        this.level = level;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
    }
    public void takeDMG(int DMG){
        currentHealth-=DMG;
    }
    public boolean die(){
        return 0 >= currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
}
