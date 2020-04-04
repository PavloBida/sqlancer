package sqlancer.cockroachdb.gen;

import java.util.List;

import sqlancer.Query;
import sqlancer.Randomly;
import sqlancer.cockroachdb.CockroachDBProvider.CockroachDBGlobalState;
import sqlancer.cockroachdb.CockroachDBSchema.CockroachDBColumn;
import sqlancer.cockroachdb.CockroachDBSchema.CockroachDBTable;

// https://www.cockroachlabs.com/docs/stable/create-index.html
public class CockroachDBIndexGenerator extends CockroachDBGenerator {

	public CockroachDBIndexGenerator(CockroachDBGlobalState globalState) {
		super(globalState);
	}

	public static Query create(CockroachDBGlobalState s) {
		return new CockroachDBIndexGenerator(s).getQuery();
	}

	@Override
	public void buildStatement() {
		errors.add("is part of the primary index and therefore implicit in all indexes");
		errors.add("already contains column");
		errors.add("violates unique constraint");
		errors.add("schema change statement cannot follow a statement that has written in the same transaction");
		errors.add("https://github.com/cockroachdb/cockroach/issues/35730"); // some array types are not indexable
		CockroachDBTable table = globalState.getSchema().getRandomTable(t -> !t.isView());
		sb.append("CREATE ");
		if (Randomly.getBoolean()) {
			sb.append("UNIQUE ");
		}
		sb.append("INDEX ON ");
		sb.append(table.getName());
		List<CockroachDBColumn> columns = table.getRandomNonEmptyColumnSubset();
		addColumns(sb, columns, true);
		if (globalState.getCockroachdbOptions().testHashIndexes && Randomly.getBoolean()) {
			sb.append(" USING HASH WITH BUCKET_COUNT=");
			sb.append(Math.min(1, Randomly.getNotCachedInteger(1, Integer.MAX_VALUE)));
			errors.add("null value in column");
			errors.add("cannot create a sharded index on a computed column");
		}
		if (Randomly.getBoolean()) {
			sb.append(" ");
			sb.append(Randomly.fromOptions("STORING", "COVERING"));
			sb.append(" ");
			addColumns(sb, table.getRandomNonEmptyColumnSubset(), false);
		}
	}

}
