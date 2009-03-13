/*
* Matrix.java -- Matrix class for Clojure that extends DenseDoubleMatrix2D
* in the CERN Colt Library
* 
* by David Edgar Liebke http://incanter.org
* March 11, 2009
* 
* Copyright (c) David Edgar Liebke, 2009. All rights reserved.  The use
* and distribution terms for this software are covered by the Eclipse
* Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
* which can be found in the file epl-v10.html at the root of this
* distribution.  By using this software in any fashion, you are
* agreeing to be bound by the terms of this license.  You must not
* remove this notice, or any other, from this software.
*/

/*
CHANGE LOG
March 11, 2009: First version
*/

package incanter;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix2D;
import clojure.lang.ISeq;
import clojure.lang.IPersistentCollection;
import clojure.lang.IPersistentVector;

public class Matrix extends DenseDoubleMatrix2D implements ISeq {

        protected boolean oneDimensional = false;

        public Matrix(int nrow, int ncol) {
                this(nrow, ncol, 0);
                if(this.rows == 1 || this.columns == 1)
                        this.oneDimensional = true;
        }

        public Matrix(int nrow, int ncol, Number initValue) {
                super(nrow, ncol);
                for(int i = 0; i < nrow; i++)
                        for(int j = 0; j < ncol; j++)
                                this.set(i, j, initValue.doubleValue());
                if(this.rows == 1 || this.columns == 1)
                        this.oneDimensional = true;
        }

        public Matrix(double[] data) {
                this(data, 1);
        }

        public Matrix(double[] data, int ncol) {
                super(data.length/ncol, ncol);
                if(this.rows == 1 || this.columns == 1)
                        this.oneDimensional = true;
                for(int i = 0; i < this.rows; i++)
                        for(int j = 0; j < this.columns; j++)
                                this.set(i, j, data[i*ncol+j]);
        }

        public Matrix(double[][] data) {
                super(data);
                if(this.rows == 1 || this.columns == 1)
                        this.oneDimensional = true;
        }

        public Matrix(DoubleMatrix2D mat) {
                this(mat.toArray());
        }

        public Matrix viewSelection(int[] rows, int[] columns) {
                Matrix mat = new Matrix(super.viewSelection(rows, columns));
                if(rows.length == 1 || columns.length == 1)
                        mat.oneDimensional = true;

                return(mat);
        }

        public Object first() {
                if(this.rows == 0 || this.columns == 0) return(null);

                int[] rows = {0};
                int[] cols = new int[this.columns];
                for(int i = 0; i < cols.length; i++) 
                        cols[i] = i;

                if(this.oneDimensional && (this.columns == 1 || this.rows == 1))
                        return(this.get(0, 0));
                else
                        return(this.viewSelection(rows, cols));
        }

        public ISeq next() {
                int initialRow = 1;
                int initialColumn = 0;

                if(!this.oneDimensional && this.rows == 1) {
                        return(null);
                }
                else if(this.oneDimensional && this.rows == 1) {
                        if(this.columns == 1)
                                return(null);
                        else {
                                initialRow = 0;
                                initialColumn = 1;
                        }
                }
                else if(this.oneDimensional && this.columns == 1) {
                        initialRow = 1;
                        initialColumn = 0;
                }

                int[] rows = new int[this.rows - initialRow];
                int[] cols = new int[this.columns - initialColumn];
                int idx = 0;
                for(int i = initialRow; i < rows.length; i++) 
                        rows[idx++] = i;
                idx = 0;
                for(int j = initialColumn; j < cols.length; j++) 
                        cols[idx++] = j;

                Matrix mat = new Matrix(this.viewSelection(rows, cols));
                mat.oneDimensional = false;
                return(mat);
        }

        public ISeq more() {
                ISeq result = this.next();
                if(result != null)
                        return result;
                else
                        return new Matrix(0, 0, 0);
        }

        public Matrix cons(Object o) {
                double[][] origData = this.toArray();

                if(o instanceof IPersistentVector) {
                        IPersistentVector v = (IPersistentVector)o;
                        double[][] newData = new double[this.rows + 1][this.columns];
                        for(int i = 0; i < (this.rows); i++)
                                for(int j = 0; j < (this.columns); j++)
                                        newData[i][j] = origData[i][j];
                        for(int j = 0; j < (this.columns); j++)
                                newData[this.rows][j] = ((Number)v.valAt(j)).doubleValue();

                        return(new Matrix(newData));
                }
                else if(o instanceof Matrix) {
                        Matrix m = (Matrix)o;
                        double[][] newData = new double[this.rows + m.rows][this.columns];
                        for(int i = 0; i < (this.rows); i++)
                                for(int j = 0; j < (this.columns); j++)
                                        newData[i][j] = origData[i][j];

                        for(int i = 0; i < m.rows; i++)
                                for(int j = 0; j < (this.columns); j++)
                                        newData[this.rows + i][j] = m.getQuick(i, j);

                        return(new Matrix(newData));
                }
                else
                        return(null);
        }

        public int count() { 
                if(this.oneDimensional) {
                        if(this.rows == 1)
                                return(this.columns);
                        else if(this.columns == 1)
                                return(this.rows);
                }
                return(this.rows); 
        }

        public Matrix seq() { 
                if(this.columns == 0 | this.columns == 0)
                        return null;
                else
                        return this; 
        }

        public IPersistentCollection empty() {
                return(new Matrix(0, 0)); 
        }

        public boolean equiv(Object o) {
                return equals(o);
                
        }
        
        public String toString() {
                StringBuffer buf = new StringBuffer();
                for(int i = 0; i < this.rows; i++) {
                        for(int j = 0; j < this.columns; j++) { 
                                buf.append(this.get(i, j));
                                buf.append("   ");
                        }
                        buf.append("\n");
                }
                return(buf.toString());
        }
        
        //public int length(){ return(this.rows); }

        //public IPersistentVector assocN(int i, Object val) { return(null); }

        //public IPersistentVector nth(int i) { return(null); }

        //public IPersistentVector valueAt(int i) { return(null); }

        //boolean containsKey(Object key);

        //public IMapEntry entryAt(Object key) {return null;}

        //public Associative assoc(Object key, Object val) {return null;}

        //public Object valAt(Object key) {return null;}

        //public Object valAt(Object key, Object notFound) {return null;}

}


