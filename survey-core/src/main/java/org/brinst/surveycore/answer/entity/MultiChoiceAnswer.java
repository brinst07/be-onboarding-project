package org.brinst.surveycore.answer.entity;

import java.util.List;

import org.brinst.surveycommon.enums.OptionType;
import org.brinst.surveycore.survey.entity.SurveyQuestion;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MultiChoiceAnswer extends AnswerParent {
	private List<String> answerValue;

	public MultiChoiceAnswer(Answer answer, SurveyQuestion surveyQuestion, List<String> answers) {
		super(answer, surveyQuestion);
		this.answerValue = answers;
	}

	@Override
	public Object getAnswerValue() {
		return answerValue;
	}

	@Override
	public OptionType getOptionType() {
		return OptionType.MULTIPLE_CHOICE;
	}
}