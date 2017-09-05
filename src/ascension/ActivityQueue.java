// TODO This is legacy code I think. No particular reason to have a full class for this.
// The model will actually be determining whether or not activities are in conflict, and
// the activity lists are well ordered to begin with. At this point, we'll just go ahead
// and ignore this class. Probably will remove in the future though.
//package ascension;
//
//import java.util.ArrayList;
//
///**
// * @author Nigel_Baldwen - nigelbaldwen@gmail.com
// * @version 1.0
// */
//
//class ActivityQueue extends ArrayList<ActivityList> {
//
//	/**
//	 * Processes the Queue and returns an ordered list.
//	 * 
//	 * <p>,
//	 * <b>Called By</b> -
//	 * <ul>
//	 * <li> {@link PrimaryModel#rotateTurn() rotateTurn()}
//	 * </ul>
//	 * <b>Calls</b> -
//	 * <ul>
//	 * <li> {@link ActivityList#organize() organize()}
//	 * </ul>
//	 * <p>
//	 * 
//	 * @return the processed list
//	 */
//	ActivityList process() {
//
//		ActivityList result = new ActivityList();
//
//		for (int i = 0; i < size(); i++) {
//			get(i).organize();
//			for (int j = 0; j < get(i).size(); j++) {
//				result.add(get(i).get(j));
//			}
//		}
//
//		result.organize();
//		return result;
//	}
//}
