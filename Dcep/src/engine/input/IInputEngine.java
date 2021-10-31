package engine.input;

public interface IInputEngine {

	public boolean isPressed(int key);

	public boolean areComboPressed(int key1, int key2);

	public boolean isActive(int key);

	public boolean isComboActive(int key1, int key2);

}
