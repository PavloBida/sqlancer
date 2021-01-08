package sqlancer.hazelcast.ast;

import sqlancer.common.ast.BinaryNode;
import sqlancer.hazelcast.HazelcastSchema.HazelcastDataType;

public class HazelcastConcatOperation extends BinaryNode<HazelcastExpression> implements HazelcastExpression {

    public HazelcastConcatOperation(HazelcastExpression left, HazelcastExpression right) {
        super(left, right);
    }

    @Override
    public HazelcastDataType getExpressionType() {
        return HazelcastDataType.TEXT;
    }

    @Override
    public HazelcastConstant getExpectedValue() {
        HazelcastConstant leftExpectedValue = getLeft().getExpectedValue();
        HazelcastConstant rightExpectedValue = getRight().getExpectedValue();
        if (leftExpectedValue == null || rightExpectedValue == null) {
            return null;
        }
        if (leftExpectedValue.isNull() || rightExpectedValue.isNull()) {
            return HazelcastConstant.createNullConstant();
        }
        String leftStr = leftExpectedValue.cast(HazelcastDataType.TEXT).getUnquotedTextRepresentation();
        String rightStr = rightExpectedValue.cast(HazelcastDataType.TEXT).getUnquotedTextRepresentation();
        return HazelcastConstant.createTextConstant(leftStr + rightStr);
    }

    @Override
    public String getOperatorRepresentation() {
        return "||";
    }

}
