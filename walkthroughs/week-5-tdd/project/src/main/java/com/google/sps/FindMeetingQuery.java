// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();
    List<TimeRange> busyTimes = new ArrayList<>();
    for (Event event : events) {
      busyTimes.add(event.getWhen());
    }
    busyTimes.sort(TimeRange.ORDER_BY_START);
    List<TimeRange> mergedBusyTimes = new ArrayList<>();
    for (TimeRange timeRange : busyTimes) {
      // If the merged list is empty or the new interval does not overlap, append it.
      // Otherwise, there must be overlap so we create a new TimeRange for the merged intervals.
      if (mergedBusyTimes.size() == 0
          || !(mergedBusyTimes.get(mergedBusyTimes.size() - 1)).overlaps(timeRange)) {
        mergedBusyTimes.add(timeRange);
      } else {
        TimeRange lastFromMergedTimes = mergedBusyTimes.remove(mergedBusyTimes.size() - 1);
        TimeRange mergedRange = TimeRange.fromStartEnd(lastFromMergedTimes.start(),
            Math.max(lastFromMergedTimes.end(), timeRange.end()), false);
        mergedBusyTimes.add(mergedRange);
      }
    }
    // Find a gap equal to the duration.
    List<TimeRange> availableTimes = new ArrayList<>();
    for (TimeRange timeRange : mergedBusyTimes) {
      System.out.println(timeRange.toString());
    }
    // throw new UnsupportedOperationException("TODO: Implement this method.");
  }
}
