package test;

public class TestBackdoor {

	public static void main(String[] args) {
		int i = 0;
		while (true) {
			System.out.println(i+" : " +(int)Math.sqrt(i) + " : " + Integer.toBinaryString(i));
			i++;
			if (i > 100) {
				break;
			}
		}
	}

}
