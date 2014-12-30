package com.github.fge.lambdas.functions.operators;

import com.github.fge.lambdas.ThrowablesFactory;
import com.github.fge.lambdas.ThrowingFunctionalInterface;
import com.github.fge.lambdas.ThrownByLambdaException;

import java.util.function.DoubleBinaryOperator;

/**
 * A throwing {@link DoubleBinaryOperator}
 */
@FunctionalInterface
public interface ThrowingDoubleBinaryOperator
    extends DoubleBinaryOperator,
    ThrowingFunctionalInterface<ThrowingDoubleBinaryOperator, DoubleBinaryOperator>
{
    double doApplyAsDouble(double left, double right)
        throws Throwable;

    @Override
    default double applyAsDouble(double left, double right)
    {
        try {
            return doApplyAsDouble(left, right);
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable tooBad) {
            throw new ThrownByLambdaException(tooBad);
        }
    }

    @Override
    default ThrowingDoubleBinaryOperator orTryWith(
        ThrowingDoubleBinaryOperator other)
    {
        return (left, right) -> {
            try {
                return doApplyAsDouble(left, right);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ignored) {
                return other.applyAsDouble(left, right);
            }
        };
    }

    @Override
    default DoubleBinaryOperator fallbackTo(DoubleBinaryOperator fallback)
    {
        return (left, right) -> {
            try {
                return doApplyAsDouble(left, right);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ignored) {
                return fallback.applyAsDouble(left, right);
            }
        };
    }

    @Override
    default <E extends RuntimeException> DoubleBinaryOperator orThrow(
        Class<E> exceptionClass)
    {
        return (left, right) -> {
            try {
                return doApplyAsDouble(left, right);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable tooBad) {
                throw ThrowablesFactory.INSTANCE.get(exceptionClass, tooBad);
            }
        };
    }

    default DoubleBinaryOperator orReturn(double defaultValue)
    {
        return (left, right) -> {
            try {
                return doApplyAsDouble(left, right);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ignored) {
                return defaultValue;
            }
        };
    }

    default DoubleBinaryOperator orReturnLeft()
    {
        return (left, right) -> {
            try {
                return doApplyAsDouble(left, right);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ignored) {
                return left;
            }
        };
    }

    default DoubleBinaryOperator orReturnRight()
    {
        return (left, right) -> {
            try {
                return doApplyAsDouble(left, right);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ignored) {
                return right;
            }
        };
    }
}