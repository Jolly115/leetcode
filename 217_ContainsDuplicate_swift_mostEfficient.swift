
func containsDuplicate(_ nums:[Int])-> Bool {

	var set = Set<Int>()
	for num in nums {
	 if( set.contains(num)){
		return true
	    }
	    set.insert(num)
	}
	return false
}


/*func containsDuplicate(_ nums: [Int]) -> Bool {
    var seen = Set<Int>()
    for num in nums {
        if seen.contains(num) {
            return true
        }
        seen.insert(num)
    }
    return false
}*/

// Example usage:
print(containsDuplicate([1, 2, 3, 1]))            // true
print(containsDuplicate([1, 2, 3, 4]))            // false
print(containsDuplicate([1,1,1,3,3,4,3,2,4,2]))   // true


