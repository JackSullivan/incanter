;;; demo.clj -- Example usage of Incanter Statistical environment

;; by David Edgar Liebke http://incanter.org
;; March 11, 2009

;; Copyright (c) David Edgar Liebke, 2009. All rights reserved.  The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software.

;; CHANGE LOG
;; March 11, 2009: First version



(ns incanter.examples.demo
  (:gen-class))


(use 'incanter.matrix)
(use 'incanter.stats)
(use 'incanter.io)

(def foo (matrix [[1 2 3] [4 5 6] [7 8 9] [10 11 12]]))
foo
(first foo)
(rest foo)
(seq foo)
(nth foo 2)
(drop 2 foo)
(reverse foo)
(cons [1 2 3] foo)
(conj foo [13 14 15])
(concat foo foo)

(apply plus foo)
(apply plus (trans foo))
(apply mult foo)
(map mean foo)
(map mean (trans foo))
(map sd foo)


(matrix 99 4 4)
(matrix 4 4)
(matrix [1 2 3 4 5 6 7 8 9] 3)

(rbind foo foo)
(rbind foo foo foo foo)
(rbind [1 2 3] foo [5 6 7] foo)

(cbind foo foo)
(cbind foo foo foo foo)
(cbind [1 2 3 4] foo [5 6 7 8] foo)

(def colt1 (matrix [[1 2 3 4] [5 6 7 8] [9 10 11 12]]))
(def colt2 (matrix [1 2 3 4 5 6 7 8 9 10 11 12] 6))
(def colt3 (matrix 1 3 3))
(def colt4 (matrix [1 2 3 4 5 6 7 8 9 10 11 12]))


(println colt1)
(println colt2)
(println colt3)

(println (sel colt1 1 0))

(println (to-vect colt1))
(println (to-vect colt2))
(println (to-vect colt3))

(println (trans colt1))
(println (trans colt2))
(println (trans colt3))

(println (mmult colt1 (trans colt1)))

(println (chol (mmult colt1 (trans colt1))))

(println (plus colt1 100))
(println (mult colt1 1/2))
(println (div colt1 10))

(println (solve (matrix [[2 0 0] [0 2 0] [0 0 2]])))

(println (minus 3 colt1))
(println (minus colt1 3))
(println (minus colt1 colt1))
(println (minus 3 3))

(println (plus 3 [1 2 3 4]))
(println (plus [1 2 3 4] 3))

(def test-mat (read-matrix (str (System/getProperty "incanter.home") "/data/test.dat")))
(def iris-mat (read-matrix (str (System/getProperty "incanter.home") "/data/iris.dat")))
(def test-csv-mat (matrix (rest (read-dataset (str (System/getProperty "incanter.home") "/data/test.csv") :delim \,))))
(def test-data (read-dataset (str (System/getProperty "incanter.home") "/data/test.dat"))) ; default delimiter: \space
(def test-csv-data (read-dataset (str (System/getProperty "incanter.home") "/data/test.csv") :delim \,))
(def test-tdd-data (read-dataset (str (System/getProperty "incanter.home") "/data/test.tdd") :delim \tab)) 

(println (sel test-mat true [0]))
(println (sel test-mat [0] true ))

;; TEST STAT FUNS

(def ols-data (read-matrix (str (System/getProperty "incanter.home") "/data/olsexamp.dat")))
(def x (sel ols-data (range 0 2313) (range 1 10)))
(def y (sel ols-data (range 0 2313) 10))


(println (covariance test-mat))
(println (covariance (sel x true 4) (sel x true 5)))
(println (sum-of-squares (sel x true 4)))
(println (sum (sel x true 4)))
(println (variance (sel x true 4)) )
(println (mean (sel x true 4)) )
(println (sd (sel x true 4)) )
(println (median (sel x true 4)))
(println (prod [1 2 3 4 5 6]) )
(print-hist (sel x true 4))
(print-hist (sel x true 4) :nbin 20 :hist-width 60)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; BAYES EXAMPLES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(use 'incanter.bayes)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(time (def b-reg-noref (bayes-regression-noref 5000 x y)))
(println (map mean (trans (:coef b-reg-noref))))
(println (mean (:var b-reg-noref)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(time (def b-reg-full (bayes-regression-full 5000 x y)))
(time (nrow (:coef b-reg-full)))
(println (map mean (trans (:coef b-reg-full))))
(println (mean (:var b-reg-full)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(time (def b-reg (bayes-regression 20000 x y)))
(println (map mean (trans (:coef b-reg))))
(println (mean (:var b-reg)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;(time (def b-reg-mh (bayes-regression-mh 20000 x y)))
;(println (map mean (trans (:coef b-reg-mh))))
;(println (mean (:var b-reg-mh)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CHARTING TESTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(use 'incanter.charts)

; test plots
(plot (histogram (rnorm 1000)))
(plot (histogram (rgamma 1000)))
(plot (histogram (runif 1000)))
(save-png (histogram (rnorm 1000)) "/Users/dliebke/Desktop/histogram.png")

(plot (histogram (rgamma 1000) 
                 :nbins 30 
                 :title "Gamma Distribution" 
                 :x-label "Value"))


(plot (scatter 
        (sel test-mat true 1) 
        (sel test-mat true 2) 
        :series-lab "Test data col 1 versus col 2"))


(def plot1 (scatter (rnorm 100) (rnorm 100)))
(plot plot1)
(add-series plot1 (rnorm 100) (rnorm 100))
(add-series plot1 (rnorm 100) (rnorm 100))
(add-series plot1 (rnorm 100) (rnorm 100))
(add-series plot1 (rnorm 100) (rnorm 100))

(set-title plot1 "new title") 
(set-x-label plot1 "new x label")
(set-y-label plot1 "new y label")


(def hist0 (histogram (rnorm 100)))
(plot hist0)
(add-series hist0 (rgamma 100))
(set-alpha hist0 0.5)

(def boxplt (boxplot (rgamma 1000))) 
(plot boxplt)
(add-series boxplt (rgamma 1000))
(add-series boxplt (rgamma 1000))
(add-series boxplt (rgamma 1000))
(add-series boxplt (rgamma 1000))


(def chart1 (xyplot (range 100) (range 100))) 
(plot chart1) 
(add-series chart1 (range 100) (mult 1/2 (range 100)))


(def x1 (range -10 10 0.01))
(def chart2 (xyplot x1 (pow x1 2)))
(plot chart2) 
(add-series chart2 x1 (mult 1/2 (pow x1 2)))


(def x2 (range 0 4 0.01))
(def chart2 (xyplot x2 (exp x1)))
(plot chart2)
