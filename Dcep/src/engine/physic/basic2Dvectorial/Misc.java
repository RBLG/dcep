package engine.physic.basic2Dvectorial;

public class Misc {

	/***
	 * produit en croix optimisé dans le cas ou la ligne est parrallele a un axe
	 * 
	 * @param lx
	 * @param px
	 * @return
	 */
	public static int flatCrossProduct(int li, int pt) {
		return (li - pt);
	}

	/***
	 * voir version non bool <br/>
	 * utilise <=, pas juste <
	 * 
	 * @param lx
	 * @param px
	 * @return
	 */
	public static boolean flatCrossProductBool(int li, int pt) {
		return (li <= pt);
	}

	/***
	 * retourne de quel coté le point pt se trouve par rapport a la ligne allant de
	 * la vers lb sous forme d'un entier positif ou negatif
	 * 
	 * @param la
	 * @param lb
	 * @param pt
	 * @return
	 */
	public static int crossProduct(FinalPoint la, FinalPoint lb, FinalPoint pt) {
		int lax, lay, lbx, lby, ptx, pty;
		lax = la.getX();
		lay = la.getY();

		lbx = lb.getX();
		lby = lb.getY();

		ptx = pt.getX();
		pty = pt.getY();

		return (lbx - lax) * (pty - lay) - (lby - lay) * (ptx - lax);

	}

	/***
	 * retourne de quel coté le point pt se trouve par rapport a la ligne allant de
	 * la vers lb sous forme d'un booleen
	 * 
	 * @param la
	 * @param lb
	 * @param pt
	 * @return
	 */
	public static boolean crossProdBool(FinalPoint la, FinalPoint lb, FinalPoint pt) {
		int lax, lay, lbx, lby, ptx, pty;
		lax = la.getX();
		lay = la.getY();

		lbx = lb.getX();
		lby = lb.getY();

		ptx = pt.getX();
		pty = pt.getY();

		return (lbx - lax) * (pty - lay) > (lby - lay) * (ptx - lax);
	}

	

}
