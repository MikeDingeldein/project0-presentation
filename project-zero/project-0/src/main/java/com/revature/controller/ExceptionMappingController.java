package com.revature.controller;

import java.security.InvalidParameterException;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.revature.dto.ExceptionMessageDTO;
import com.revature.exceptions.ClientNotFound;

import io.javalin.Javalin;

public class ExceptionMappingController {

	public void mapException(Javalin app) {
		app.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
			ctx.status(400);
			ctx.json(new ExceptionMessageDTO(e));
		});

		app.exception(InvalidParameterException.class, (e, ctx) -> {
			ctx.json(new ExceptionMessageDTO(e));
			ctx.status(400);
		});

		app.exception(ClientNotFound.class, (e, ctx) -> {
			ctx.json(new ExceptionMessageDTO(e));
			ctx.status(404);

		});
	}

}
