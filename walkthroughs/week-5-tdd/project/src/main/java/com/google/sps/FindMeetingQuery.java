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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long duration = request.getDuration();
    // The duration should not be longer than 1 day.
    if (duration > TimeRange.getTimeInMinutes(23, 59)) {
      return Arrays.asList();
    }
    Collection<String> attendees = request.getAttendees();
    List<TimeRange> busyTimes = new ArrayList<>();

    // Find the other events the attendees are already scheduled in for.
    for (Event event : events) {
      if (attendees.stream().anyMatch(x -> event.getAttendees().contains(x))) {
        busyTimes.add(event.getWhen());
      }
    }

    busyTimes.sort(TimeRange.ORDER_BY_START);
    List<TimeRange> mergedBusyTimes = new ArrayList<>();
    for (TimeRange unavailableTimeRange : busyTimes) {
      // If the merged list is empty or the new interval does not overlap, append it.
      // Otherwise, there must be overlap so we create a new TimeRange for the merged intervals.
      if (mergedBusyTimes.size() == 0
          || !(mergedBusyTimes.get(mergedBusyTimes.size() - 1)).overlaps(unavailableTimeRange)) {
        mergedBusyTimes.add(unavailableTimeRange);
      } else {
        TimeRange oldMergedTime = mergedBusyTimes.remove(mergedBusyTimes.size() - 1);
        int startTime = oldMergedTime.start();
        int endTime = Math.max(oldMergedTime.end(), unavailableTimeRange.end());
        TimeRange updatedMergeRange = TimeRange.fromStartEnd(startTime, endTime, false);
        mergedBusyTimes.add(updatedMergeRange);
      }
    }

    List<TimeRange> availableTimes = new ArrayList<>();
    int initialStart = TimeRange.START_OF_DAY;

    // Find a gap greater than or equal to the duration.
    for (TimeRange unavailableTimeRange : mergedBusyTimes) {
      if (unavailableTimeRange.start() - initialStart >= duration) {
        availableTimes.add(
            TimeRange.fromStartEnd(initialStart, unavailableTimeRange.start(), false));
      }
      initialStart = unavailableTimeRange.end();
    }

    // Check if the event can be scheduled at the end of the day.
    if (TimeRange.END_OF_DAY - initialStart >= duration) {
      availableTimes.add(TimeRange.fromStartEnd(initialStart, TimeRange.END_OF_DAY, true));
    }
    return availableTimes;
  }
}
