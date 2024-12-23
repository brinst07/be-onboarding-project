package org.brinst.surveycore.answer.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.brinst.surveycommon.enums.ErrorCode;
import org.brinst.surveycommon.exception.GlobalException;
import org.brinst.surveycore.answer.dto.AnswerItemDTO;
import org.brinst.surveycore.answer.validator.AnswerValidator;
import org.brinst.surveycore.survey.entity.SurveyQuestion;
import org.brinst.surveycore.survey.entity.SurveyVersion;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidatorService {
	private final List<AnswerValidator> validators;

	public void validate(SurveyVersion surveyVersion, List<AnswerItemDTO> answerDTO) {
		Map<Long, SurveyQuestion> surveyQuestionMap = surveyVersion.getSurveyQuestions().stream()
			.collect(Collectors.toMap(SurveyQuestion::getId, surveyQuestion -> surveyQuestion));
		Map<Long, AnswerItemDTO> answerItemIds = answerDTO.stream()
			.collect(Collectors.toMap(AnswerItemDTO::getItemId, answer -> answer));
		surveyQuestionMap.forEach((id, question) -> {
			AnswerItemDTO reqDTO = answerItemIds.get(id);
			// reqDTO가 null이고 question이 필수 응답이 아닌 경우 검증 로직 건너뛰기
			if (reqDTO == null) {
				if (!question.isRequired()) {
					return;
				} else {
					throw new GlobalException(ErrorCode.BAD_REQUEST, "Answer is required.");
				}
			}

			// reqDTO가 존재하거나 필수 응답일 경우 검증 로직 수행
			if (reqDTO.getAnswerValue() == null && question.isRequired()) {
				throw new GlobalException(ErrorCode.BAD_REQUEST, "Answer is required.");
			}

			// 각 Validator 실행
			validators.forEach(validator -> validator.validate(question, reqDTO));
		});
	}
}
