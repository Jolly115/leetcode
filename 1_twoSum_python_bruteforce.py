


def two_sum(arr, target):
    for i in range(len(arr)):
        for j in range(i + 1, len(arr)):  # avoid duplicate pairs
            if arr[i] + arr[j] == target:
                return [i, j]
    return None

print(two_sum([1,2,3,4,5],9))
