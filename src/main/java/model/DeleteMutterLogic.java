package model;

import dao.MuttersDAO;

/**
 * DeleteMutterLogic - つぶやき削除ロジック
 * ログインユーザーが自分の投稿のみ削除できるようにします
 */
public class DeleteMutterLogic {
	
	/**
	 * つぶやきを削除
	 * @param mutterId 削除対象の投稿ID
	 * @param loginUserName ログイン中のユーザー名
	 * @return 削除成功時は true、失敗時は false
	 */
	public boolean execute(int mutterId, String loginUserName) {
		// ユーザー名が null または空文字列の場合は削除不可
		if (loginUserName == null || loginUserName.isEmpty()) {
			return false;
		}
		
		MuttersDAO dao = new MuttersDAO();
		// ユーザー名を条件に含めることで、他人の投稿は削除できなくする
		return dao.delete(mutterId, loginUserName);
	}
}
