package com.ch.common.entity;

import com.ch.common.config.MyException;
import lombok.Data;

/**
 * @author ch265357 2019-04-03 15:15
 */
@Data
public class Select {
	private String field;
	private Operation operation;
	private Object value;
	private Class aClass;
	public enum Operation {
		EQ("="),
		LIKE("like"),
		START(">"),
		END("<");
		private String value;

		Operation(String value) {
			this.value = value;
		}
	}

	public static Select getSelect(String selects,boolean state) {
		Object[] split = selects.split(" ");
		Select select = new Select();
		select.setField(split[0].toString());
		switch (split[1].toString()) {
			case "=":
				select.setOperation(Operation.EQ);
				break;
			case "like":
				select.setOperation(Operation.LIKE);
				break;
			case ">":
				select.setOperation(Operation.START);
				break;
			case "<":
				select.setOperation(Operation.END);
				break;
			default: throw new MyException("400","no such operation");
		}
		String value = split[2].toString();
		if (state) {
			select.setValue(Integer.parseInt(value));
		} else {
			select.setValue(value);
		}
		return select;
	}
}
