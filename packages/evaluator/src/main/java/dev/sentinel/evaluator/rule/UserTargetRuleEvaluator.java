package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UserTargetRuleEvaluator implements RuleEvaluator {

  private static final Pattern INCLUDED_USER_IDS_PATTERN =
      Pattern.compile("\"includedUserIds\"\\s*:\\s*\\[(.*?)\\]");

  @Override
  public boolean supports(FlagRule rule) {
    return rule.ruleType() == RuleType.USER_TARGET;
  }

  @Override
  public EvaluationResult evaluate(EvaluationRequest request, FlagRule rule) {
    Matcher matcher = INCLUDED_USER_IDS_PATTERN.matcher(rule.configJson());
    if (!matcher.find()) {
      return null;
    }

    String userId = request.userIdentifier().value();
    String[] entries = matcher.group(1).split(",");
    for (String entry : entries) {
      String normalizedEntry = entry.replace("\"", "").trim();
      if (userId.equals(normalizedEntry)) {
        return buildResult(request, true, "user_target", rule.id());
      }
    }

    return null;
  }

  private EvaluationResult buildResult(
      EvaluationRequest request, boolean enabled, String reason, UUID matchedRuleId) {
    return new EvaluationResult(
        request.flagKey(),
        enabled,
        reason,
        matchedRuleId,
        Instant.now());
  }
}
