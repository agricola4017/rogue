public class Character {
    //private int level;
    private int hp;
    private int attack;
    private int defense;
    private int wins;

    public Character(int hp, int attack, int defense) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.wins = 0;
    }
    @Override
    public String toString() {
        return "{" +
                "hp=" + hp +
                ", attack=" + attack +
               // ", defense=" + defense +
                '}';
    }

    public void atkUp() {
        this.attack +=1;
    }

    public void hpUp() {
        this.hp +=3;
    }

    public void restoreHP(int round) {
        int heal = (int)Math.round(0.5*(3*round - this.hp));
        this.hp += heal;
    }
    public static boolean fight(Character a, Character b) {
        while (a.getHp() >= 0 && b.getHp() >+ 0) {
            a.setHp(a.getHp()-b.getAttack());
            b.setHp(b.getHp()-a.getAttack());
        }

        return a.getHp() >= 0;
    }

    public static Character generateEnemy(int level) {
        int factor = 2;
        int hp = (int)Math.round(Math.random()*level*factor*2);
        int atk = (int)Math.round(Math.random()*level);
        int def = (int)Math.round(Math.random()*level*factor);

        //System.out.println("stats are " + Math.random() +  level + " " + factor);
        return new Character(hp, atk, def);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
