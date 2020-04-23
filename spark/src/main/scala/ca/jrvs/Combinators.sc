import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
 * Count number of elements
 * Get the first element
 * Get the last element
 * Get the first 5 elements
 * Get the last 5 elements
 *
 * hint: use the following methods
 * head
 * last
 * size
 * take
 * tails
 */
val ls = List.range(0,10)
//write you solution here

//Number of elements
println(ls.size)

// first element
println(ls(0))

//last element
println(ls.last)

//first 5 elements
println(ls.take(5))

//last 5 elements
println(ls.takeRight(5))


/**
 * Double each number from the numList and return a flatten list
 * e.g.res4: List[Int] = List(2, 3, 6)
 *
 * Compare flatMap VS ls.map().flatten
 */
val numList = List(List(1,2), List(3));
//write you solution here
//flatten
println(numList.flatten.map((n:Int) => n*2))
//flatMap
println(numList.flatMap((list2:List[Int]) => list2.map((n:Int) => n*2)))

// flatten is simpler above since flatMap requires mapping twice before flattening occurs.
// Overall easier to map an already flattened list

/**
 * Sum List.range(1,11) in three ways
 * hint: sum, reduce, foldLeft
 *
 * Compare reduce and foldLeft
 * https://stackoverflow.com/questions/7764197/difference-between-foldleft-and-reduceleft-in-scala
 */
//write you solution here
println(ls.sum)
println(ls.reduce(_+_))
println(ls.foldLeft(0) {_+_})
//

/**
 * Practice Map and Optional
 *
 * Map question1:
 *
 * Compare get vs getOrElse (Scala Optional)
 * countryMap.get("Amy");
 * countryMap.getOrElse("Frank", "n/a");
 */
val countryMap = Map("Amy" -> "Canada", "Sam" -> "US", "Bob" -> "Canada")
countryMap.get("Amy")
countryMap.get("edward")
countryMap.getOrElse("edward", "n/a")
// using get, if Some value is found would return that value, if not will return None.
// using getOrElse, we can return an alternative value if we cannot find the value.

/**
 * Map question2:
 *
 * create a list of (name, country) tuples using `countryMap` and `names`
 * e.g. res2: List[(String, String)] = List((Amy,Canada), (Sam,US), (Eric,n/a), (Amy,Canada))
 */
val names = List("Amy", "Sam", "Eric", "Amy")
//write you solution here
val pairing = names.map( (name:String) => "(" + name + "," + countryMap.getOrElse(name, "n/a") + ")")
println(pairing)

/**
 * Map question3:
 *
 * count number of people by country. Use `n/a` if the name is not in the countryMap  using `countryMap` and `names`
 * e.g. res0: scala.collection.immutable.Map[String,Int] = Map(Canada -> 2, n/a -> 1, US -> 1)
 * hint: map(get_value_from_map) ; groupBy country; map to (country,count)
 */
//write you solution here
val newMap = names.foldLeft(Map.empty[String,Int]) ((mapper: Map[String,Int],name: String) => {
  var country = countryMap.getOrElse(name,"n/a")
  mapper + ( country -> (mapper.getOrElse(country,0) + 1) ) }
)
println(newMap)




/**
 * number each name in the list from 1 to n
 * e.g. res3: List[(Int, String)] = List((1,Amy), (2,Bob), (3,Chris))
 */
val names2 = List("Amy", "Bob", "Chris", "Dann")
//write you solution here
val names2Size = 1 to names2.size toList
val res = names2Size zip names2
println(res)



/**
 * SQL questions1:
 *
 * read file lines into a list
 * lines: List[String] = List(id,name,city, 1,amy,toronto, 2,bob,calgary, 3,chris,toronto, 4,dann,montreal)
 */
//write you solution here
val doc = Source.fromFile("/home/yangdav9/dev/jarvis_data_eng_david/spark/src/main/scala/ca/jrvs/resources/employees.csv").getLines.toList
/**
 * SQL questions2:
 *
 * Convert lines to a list of employees
 * e.g. employees: List[Employee] = List(Employee(1,amy,toronto), Employee(2,bob,calgary), Employee(3,chris,toronto), Employee(4,dann,montreal))
 */
//write you solution here
val dropHeader = doc.drop(1)
case class Employee(id:Int, name:String, city:String, age:Int)
var bufList = new ListBuffer[Employee]()
println(dropHeader)

dropHeader.foreach{
  row => val values = row.split(",")
    val tempEmployee = Employee(values(0).toInt, values(1),values(2),values(3).toInt)
    bufList += tempEmployee
}
println(bufList.toList)

/**
 * SQL questions3:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 *
 * result:
 * upperCity: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(2,bob,CALGARY,19), Employee(3,chris,TORONTO,20), Employee(4,dann,MONTREAL,21), Employee(5,eric,TORONTO,22))
 */
//write you solution here
val upperCity = bufList.map((emp:Employee) => new Employee(emp.id,emp.name,emp.city.toUpperCase,emp.age))
println(upperCity.toList)



/**
 * SQL questions4:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 * WHERE city = 'toronto'
 *
 * result:
 * res5: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(3,chris,TORONTO,20), Employee(5,eric,TORONTO,22))
 */
//write you solution here
val torontoCity = upperCity.filter(_.city=="TORONTO")
println(torontoCity.toList)

/**
 * SQL questions5:
 *
 * Implement the following SQL logic using functional programming
 *
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city
 *
 * result:
 * cityNum: scala.collection.immutable.Map[String,Int] = Map(CALGARY -> 1, TORONTO -> 3, MONTREAL -> 1)
 */
//write you solution here
val cityCount = upperCity.toList.foldLeft(Map.empty[String,Int]) ((mapper: Map[String,Int],curEmploy: Employee) => {
  mapper + ( curEmploy.city -> (mapper.getOrElse(curEmploy.city,0) + 1) ) }
)
println(cityCount)

/**
 * SQL questions6:
 *
 * Implement the following SQL logic using functional programming
 *
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city,age
 *
 * result:
 * res6: scala.collection.immutable.Map[(String, Int),Int] = Map((MONTREAL,21) -> 1, (CALGARY,19) -> 1, (TORONTO,20) -> 2, (TORONTO,22) -> 1)
 */
//write you solution here
val cityAge = upperCity.toList.foldLeft(Map.empty[(String,Int),Int]) ((mapper: Map[(String,Int),Int],curEmploy: Employee) => {
  val cityAgeTuple = (curEmploy.city,curEmploy.age)
  mapper + ( cityAgeTuple -> (mapper.getOrElse(cityAgeTuple,0) + 1) ) }
)
println(cityAge)
