#Author: Miguel

import numpy as np
import argparse
import cv2

ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", help = "path")
args = vars(ap.parse_args())

image = cv2.imread(args["image"])
rows,cols,channels = image.shape

boundaries = [
	([89, 106, 130], [127, 139, 156])
	#([103, 86, 65], [145, 133, 128])
]

for (lower, upper) in boundaries:
	lower = np.array(lower, dtype = "uint8")
	upper = np.array(upper, dtype = "uint8")

	mask = cv2.inRange(image, lower, upper)
	output = cv2.bitwise_and(image, image, mask = mask)
	output[np.where((output >[0, 0, 0]).all(axis = 2))] = [255,0,0]
	im = cv2.bitwise_or(output, image)


	

	cv2.imshow("images", np.hstack([im]))
	cv2.waitKey(0)
