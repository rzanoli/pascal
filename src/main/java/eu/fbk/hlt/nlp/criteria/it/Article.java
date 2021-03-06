package eu.fbk.hlt.nlp.criteria.it;

import eu.fbk.hlt.nlp.cluster.Keyphrase;
import eu.fbk.hlt.nlp.cluster.Keyphrases;
import eu.fbk.hlt.nlp.cluster.Language;
import eu.fbk.hlt.nlp.criteria.AbstractArticle;

/*
* 
* Criteria: Article
* 
* Version: 1.2
* 
* Definition (2018-02-22):
* 
* kj and ki are variants if they have the same number of tokens but one contains a simple preposition 
* (TextPro PoS E) and the other one contains an article/preposition contraction (TextPro PoS ES or EP), 
* where the lemma of the simple preposition is the same as the part of the lemma of the contraction before “/det”  
*
* E.g.,
* regole di gioco (PoS E with lemma di) for regole del gioco (PoS ES with lemma di/det)
* 
* @author zanoli
* 
*/
public class Article extends AbstractArticle {

	// version
	public static final String version = "1.2";
	// language
	public static final Language language = Language.IT;
	
	/**
	 * Given a keyphrase key1, can the keyphrase key2 be derived from key1?
	 * 
	 * @param key1
	 *            the keyphrase key1
	 * @param key2
	 *            the keyphrase key2
	 * 
	 * @return if key2 can be derived from key1
	 */
	public static boolean evaluate(Keyphrase key1, Keyphrase key2) {

		if (key1.length() != key2.length()) {
			return false;
		}

		int articlesCount = 0;
		for (int i = 0; i < key1.length(); i++) {

			if (key1.get(i).getForm().equals(key2.get(i).getForm())) {
				
			} else if ((key1.get(i).getPoS().equals("E")
					&& (key2.get(i).getPoS().equals("ES") || key2.get(i).getPoS().equals("EP"))
					&& key2.get(i).getLemma().indexOf("/det") != -1
					&& key1.get(i).getLemma()
							.equals(key2.get(i).getLemma().substring(0, key2.get(i).getLemma().indexOf("/det"))))
					|| ((key1.get(i).getPoS().equals("ES") || key1.get(i).getPoS().equals("EP"))
							&& key2.get(i).getPoS().equals("E") 
							&& key1.get(i).getLemma().indexOf("/det") != -1
							&& key2.get(i).getLemma().equals(
									key1.get(i).getLemma().substring(0, key1.get(i).getLemma().indexOf("/det")))))
				articlesCount++;
			else {
				return false;
			}

		}

		return (articlesCount == 1);

	}
	
}
