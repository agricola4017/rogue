public class Character {
    public enum CharacterType {
        WARRIOR(5, 3, 4),
        ARCHER(3, 4, 2),
        MAGE(2, 5, 1);

        private final int baseHp;
        private final int baseAttack;
        private final int baseDefense;

        CharacterType(int baseHp, int baseAttack, int baseDefense) {
            this.baseHp = baseHp;
            this.baseAttack = baseAttack;
            this.baseDefense = baseDefense;
        }
    }

    private CharacterType type;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int position; // 0-5 for positioning
    private boolean isAlive;

    public Character(CharacterType type, int position) {
        this.type = type;
        this.hp = type.baseHp;
        this.maxHp = type.baseHp;
        this.attack = type.baseAttack;
        this.defense = type.baseDefense;
        this.position = position;
        this.isAlive = true;
    }

    @Override
    public String toString() {
        return type + "{" +
                "hp=" + hp + "/" + maxHp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", pos=" + position +
                '}';
    }

    public void atkUp() {
        this.attack += 1;
    }

    public void hpUp() {
        this.maxHp += 3;
        this.hp += 3;
    }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - this.defense);
        this.hp -= actualDamage;
        if (this.hp <= 0) {
            this.hp = 0;
            this.isAlive = false;
        }
    }

    public Character findTarget(Character[] enemies) {
        // Simple targeting logic: target closest enemy
        Character target = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Character enemy : enemies) {
            if (enemy != null && enemy.isAlive) {
                int distance = Math.abs(this.position - enemy.position);
                if (distance < minDistance) {
                    minDistance = distance;
                    target = enemy;
                }
            }
        }
        return target;
    }

    public static Character generateEnemy(int level, int position) {
        CharacterType[] types = CharacterType.values();
        CharacterType randomType = types[(int)(Math.random() * types.length)];
        Character enemy = new Character(randomType, position);
        
        // Scale stats based on level
        for (int i = 1; i < level; i++) {
            enemy.atkUp();
            enemy.hpUp();
        }
        
        return enemy;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public CharacterType getType() {
        return type;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }
}
