package com.example.onederful.domain.log.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.example.onederful.domain.log.entity.Log;
import com.example.onederful.domain.log.enums.Activity;

public class LogSpecification {
	public static Specification<Log> hasUserId(Long userId) {
		return (root, query, builder) ->
			userId == null ? null : builder.equal(root.get("user").get("id"), userId);
	}

	public static Specification<Log> hasActivity(Activity activity) {
		return (root, query, builder) ->
			activity == null ? null : builder.equal(root.get("activity"), activity);
	}

	public static Specification<Log> hasTargetId(Long targetId) {
		return (root, query, builder) ->
			targetId == null ? null : builder.equal(root.get("targetId"), targetId);
	}

	public static Specification<Log> betweenDates(LocalDate start, LocalDate end) {
		return (root, query, builder) -> {
			// 둘다 없을 경우
			if (start == null && end == null) return null;
			// 둘다 있을 경우 -> between
			// start.atStartOfDay() = 00-01-01(시작날) 00:00:00
			// end.plusDays(1).atStartOfDay() = (00-01-02(종료날)일 경우) 00-01-03 00:00:00
			if (start != null && end != null)
				return builder.between(root.get("createdAt"), start.atStartOfDay(), end.plusDays(1).atStartOfDay());
			// 시작날만 있을 경우
			if (start != null)
				return builder.greaterThanOrEqualTo(root.get("createdAt"), start.atStartOfDay());
			// 종료날만 있을 경우
			return builder.lessThan(root.get("createdAt"), end.plusDays(1).atStartOfDay());
		};
	}
}
