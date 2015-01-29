package de.math;

/**
 * @author Christian Köditz
 * 
 * Repräsentiert eine Matrix zum Lösen Linearer Gleichungssysteme
 *
 */
/**
 * @author ko
 *
 */
public class Matrix {
	/**
	 * Beinhaltet die Felder der Matrix
	 */
	private final Double[][] ma;

	/**
	 * @param values [][] values: Werte der n x m Matrix
	 */
	public Matrix(final Double[][] values) {
		this.ma = values;
		sort(ma);
	}

	/**
	 * Löst die Matris als Lineares Gleichungssystem
	 * 
	 * @param inputMA
	 *            zu berechnende Matrix
	 * @return Verhältnisse
	 */
	private Double[] calculate(final Double[][] inputMA) {
		// recursiev
		// Löse LGS. Jeder durchlauf eine stufe weniger
		Double[][] localMA = new Double[inputMA.length - 1][inputMA[0].length];

		Double[] tempArray;

		for (int i = 0; i < localMA.length; i++) {
			tempArray = new Double[inputMA.length];
			// Erster Wert eine Zeile wird mint dem ersten Wert der
			// abzuziehenden Zeile Multipliziert. So wird die erste spalte immer
			// 0
			for (int j = 0; j < inputMA[0].length - 1; j++) {
				tempArray[j] = inputMA[i + 1][0] * inputMA[0][j + 1];
				tempArray[j] -= inputMA[0][0] * inputMA[i + 1][j + 1];
			}
			// neue Gleichung (Reihe) schreiben
			localMA[i] = tempArray;
		}

		// beim rückwerts einsetzen darf der neu zu bestimmente wert nicht 0 mal
		// vorkommen
		// ges x 0x+4y=5 geg y=3 ist nicht für x lösbar deshalb sortieren
		sort(localMA);
		tempArray = localMA[0];

		Double[] result;

		if (localMA.length > 1) {
			result = calculate(localMA);

			return multiplyOut(result, tempArray);

		} else {
			result = new Double[1];
			result[0] = tempArray[1] / tempArray[0];

			return result;
		}
	}

	/**
	 * Rückwerts einsetzen
	 * 
	 * @param a1
	 *            Einzusetzende Werte
	 * @param a2
	 *            Gleichung in welche eingesetzt werden soll
	 * @return Lösung
	 */
	private Double[] multiplyOut(final Double[] a1, final Double[] a2) {

		for (int i = 0; i < a1.length; i++) {
			a2[i + 1] *= a1[i];
			a2[a2.length - 1] -= a2[i + 1];
		}

		// no unshift in Java
		// a1.unshift(a2[a2.length-1] / a2[0]);
		final Double[] result = new Double[a1.length + 1];
		final double tmp = a2[a2.length - 1] / a2[0];
		result[0] = tmp;
		for (int i = 1; i < result.length; i++) {
			result[i] = a1[i - 1];
		}

		return result;
	}

	/**
	 * @param inputMA
	 *            sortieren der Matrix. Keine 0 an der ersten Stelle der ersten
	 *            Zeile
	 */
	private void sort(final Double[][] inputMA) {
		// erste spalte der ersten zeile darf nicht 0 seien
		if (inputMA[0][0] != 0)
			return;

		for (int i = 0; i < inputMA.length; i++) {
			if (inputMA[i][0] == 0)
				continue;
			final Double[] tmp = inputMA[0];
			inputMA[0] = inputMA[i];
			inputMA[i] = tmp;
			return;
		}
	}

	/**
	 * Löst die Matrix
	 * 
	 * @return Lösung für die Stellen
	 */
	public Double[] getResult() {

		final Double[] result = calculate(ma);

		final Double[] tempArray = ma[0];

		return multiplyOut(result, tempArray);
	}
}
