package com.p6majo.transformation;

public interface TransformableObject {
    TransformableObject performTransformation(Transformation transformation);
    TransformableObject copy();
}
