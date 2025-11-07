fun containsDuplicate(nums: IntArray): Boolean {
    val seen = mutableSetOf<Int>()
    for (num in nums) {
        if (num in seen) return true
        seen.add(num)
    }
    return false
}

fun main() {
    val nums1 = intArrayOf(1, 2, 3, 1)
    val nums2 = intArrayOf(1, 2, 3, 4)
    val nums3 = intArrayOf(1, 1, 1, 3, 3, 4, 3, 2, 4, 2)

    println(containsDuplicate(nums1)) // true
    println(containsDuplicate(nums2)) // false
    println(containsDuplicate(nums3)) // true
}

