package tpslogger.tpslogger.client;

public class PlayerInfo {
	public String name;
	public boolean afk;
	public String rank;
	public PlayerInfo(String name) {
		String[] parts=name.split(" ");
		this.name=parts[1];
		this.rank=parts[0].split("]")[0].split("\\[")[1];
		this.afk=!parts[0].split("\\[")[0].isEmpty();
	}

	@Override
	public String toString() {
		return (afk?"*":"")+"["+rank+"]" + name;
	}
}
