package model;

import java.util.List;
import java.util.ArrayList;

import dao.MuttersDAO;

public class GetMutterListLogic {
	public List<Mutter> execute() {
		MuttersDAO dao = new MuttersDAO();
		List<Mutter> mutterList = dao.findAll();
		if (mutterList == null) {
			mutterList = new ArrayList<Mutter>();
		}
		return mutterList;
	}
}
