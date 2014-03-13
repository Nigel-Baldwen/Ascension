package ascension;

import java.util.ArrayList;

public class ActivityQueue extends ArrayList<ActivityList> {

	public ActivityList process() {

		ActivityList result = new ActivityList();

		for (int i = 0; i < size(); i++) {
			get(i).organize();
			for (int j = 0; j < get(i).size(); j++) {
				result.add(get(i).get(j));
			}
		}

		return result;
	}
}
