import matplotlib.pyplot as plt
import string

pidout = open("pidout.txt").readlines()

p = []
d = []
i = []
bound = []
score = []

count = 0

while count<len(pidout):
    if "p" in pidout[count]:
        count+=1
        p = pidout[count].strip(string.whitespace).strip("[]").split(", ")
        count+=1
    if "d" in pidout[count]:
        count+=1
        d = pidout[count].strip(string.whitespace).strip("[]").split(", ")
        count+=1
    if "i" in pidout[count]:
        count+=1
        i = pidout[count].strip(string.whitespace).strip("[]").split(", ")
        count+=1
    if "bound" in pidout[count]:
        count+=1
        bound = pidout[count].strip(string.whitespace).strip("[]").split(", ")
        count+=1
    if "score" in pidout[count]:
        count+=1
        score = pidout[count].strip(string.whitespace).strip("[]").split(", ")
        count+=1
for index in range(len(score)):
    p[index] = float(p[index])
    d[index] = float(d[index])
    i[index] = float(i[index])
    bound[index] = float(bound[index])
    score[index] = float(score[index])
plt.subplot(221)
plt.plot(p, score, 'ro')
plt.title("p vs score")
plt.subplot(222)
plt.plot(d, score, 'ro')
plt.title("d vs score")
plt.subplot(223)
plt.plot(i, score, 'ro')
plt.title("i vs score")
plt.subplot(224)
plt.plot(bound, score, 'ro')
plt.title("bound vs score")
plt.figure()

score_color = []
for s in score:
    if s > 3500:
        score_color.append(3500)
    else:
        score_color.append(s)
plt.scatter(d, p, c=score_color)
plt.gray()
plt.show()