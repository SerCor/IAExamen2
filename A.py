#!/bin/python3

import math
import os
import random
import re
import sys

class Node:
    def __init__(self, arr):
        self.value = len(set(arr))
        self.arr = arr
        self.parent = None
        self.H = 0
        self.G = 0


def children(arr):
    s = list(set(arr))
    children = []
    for deleted_element in s:
        arr_copy = list(arr)
        arr_copy.remove(deleted_element)
        child = Node(arr_copy)
        children.append(child)
    return children
    # return [Node([x for x in arr if x!= deleted_element]) for deleted_element in s] # issue


def astar(start, goal):
    # the open and closed sets
    openset = []
    closedset = []
    # current point is the starting point
    current = start
    #add the starting point to the openset
    openset.append(current)
    # while the openset is not empty
    while openset:
        current = min(openset, key=lambda o:o.G + o.H)
        # if it is the item we want retrace the path and return it
        if len(set(current.arr)) == goal:
            print("inside, current.arr: " + str(current.arr))
            path = []
            while current.parent:
                print("path in while: " + str([x.arr for x in path]))
                path.append(current)
                current = current.parent
            print("path: " + str([x.arr for x in path]))
            path.append(current)
            print("path: " + str([x.arr for x in path]))
            return path[::-1]
        # remove the item from the openset
        openset.remove(current)
        # add it to the closed set
        closedset.append(current)
        # loop through the node's children and siblings
        print("current.arr: " + str(current.arr))
        for node in children(current.arr):
            #If it is already in the closed set, skip it
            if node in closedset:
                continue
            #Otherwise if it is already in the open set
            if node in openset:
                # check if we beat the G score
                new_g = current.G + 1
                if node.G > new_g:
                    # if so, update the node to have a new parrent
                    node.G = new_G
                    node.parent = current

            else:
                # if it isn't in the openset, calculate the G and H score for the node
                node.G = current.G
                node.H = len(set(current.arr))
                #Set the parent to our current item
                node.parent = current
                #add it to the set
                openset.append(node)
    # thow an error if path is not found
    raise ValueError('No Path Found')

# Complete the equalizeArray function below.
def equalizeArray(arr):
    startNode = Node(arr)
    path = astar(startNode,1)
    path.pop(-1)
    print("path: " + str(path))
    return len(path)

if __name__ == '__main__':
    fptr = open(os.environ['OUTPUT_PATH'], 'w')

    n = int(input())

    arr = list(map(int, input().rstrip().split()))

    result = equalizeArray(arr)

    fptr.write(str(result) + '\n')

    fptr.close()