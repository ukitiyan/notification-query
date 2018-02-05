package config;

import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

import functions.Function.Input;
import functions.Function.Output;

public class Handler extends SpringBootRequestHandler<Input, Output> {
}