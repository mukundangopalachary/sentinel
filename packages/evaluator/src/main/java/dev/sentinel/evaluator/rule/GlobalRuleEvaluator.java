package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GlobalRuleEvaluator implements RuleEvaluator {

  private static final Pattern ENABLED_PATTERN =
      Pattern.compile("\"enabled\"\\s*:\\s*(true|false)");

  @Override
  public boolean supports(FlagRule rule) {
    return rule.ruleType() == RuleType.GLOBAL;
  }

  @Override
  public EvaluationResult evaluate(EvaluationRequest request, FlagRule rule) {
    Matcher matcher = ENABLED_PATTERN.matcher(rule.configJson());
    if (!matcher.find()) {
      return null;
    }

    boolean enabled = Boolean.parseBoolean(matcher.group(1));
    String reason = enabled ? "global_enabled" : "global_disabled";
    return buildResult(request, enabled, reason, rule.id());
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
