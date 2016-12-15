import matplotlib.pyplot as plt

#constants copy/pasted from debug mode
intercept = 8084.871906554297
d1 = -1141.8549896106242
p1 = -2445.811380308306
i1 = 1.9947256636992335E7
bound1 = 0.0
d2 = -1981.3847716248988
p2 = -1354.694794667785
i2 = -9.294460360215134E10
pd = -2489.784897663193
pi = 1.9000910194446396E7
id = 1.3712335994477035E7
bound2 = 0.0
d3 = 307.12184096966877
p3 = 430.583718265032
i3 = 8.038110541676366E13
pd2 = 969.3621307934127
p2d = 682.2340006152165
id2 = -197011.6002192796
p2i = -1789479.1804563808
i2d = -3.379490458124696E9
pi2 = -8.328492167642594E9
pid = -2783640.326514328
bound3 = 0.0

#ranges
D_UPPER = 1.5
D_LOWER = 0.0
D_RANGE = D_UPPER - D_LOWER
P_UPPER = 1.5
P_LOWER = 0.0
P_RANGE = P_UPPER - P_LOWER
I_UPPER = 0.0005
I_LOWER = 0.0
I_RANGE = I_UPPER - I_LOWER
numtests = 50

def gradientD(d, p, i):
    return (d1) + (d2 * d) + (pd * p) + (id * i) + (d3 * d * d) + (pd2 * p * d) + (p2d * p * p) + (id2 * i * d) + (i2d * i * i) + (pid * p * i);


def gradientP(d, p, i):
    return (p1) + (p2 * p) + (pd * d) + (pi * i) + (p3 * p * p) + (pd2 * d * d) + (p2d * p * d) + (p2i * p * i) + (pi2 * i * i) + (pid * i * d);


def gradientI(d, p, i):
    return (i1) + (i2 * i) + (pi * p) + (id * d) + (i3 * i * i) + (id2 * d * d) + (p2i * p * p) + (i2d * i * d) + (pi2 * p * i) + (pid * p * d)

gradD = []
gradP = []
gradI = []
dterms = []
pterms = []
iterms = []

dstep = D_RANGE/numtests
pstep = P_RANGE/numtests
istep = I_RANGE/numtests

d = D_LOWER
p = P_LOWER
i = I_LOWER

while d < D_UPPER:
    p = P_LOWER
    while p < P_UPPER:
        i = I_LOWER
        while i < I_UPPER:
            dterms.append(d)
            pterms.append(p)
            iterms.append(i)

            gradD.append(gradientD(d, p, i))
            gradP.append(gradientP(d, p, i))
            gradI.append(gradientI(d, p, i))

            i+=istep
        p+=pstep
    d+=dstep
    
    

plt.figure()
plt.subplot(221)
plt.scatter(dterms, gradD, c=iterms)
plt.title("dterms vs gradD")
plt.gray()
plt.subplot(222)
plt.scatter(pterms, gradD, c=iterms)
plt.title("pterms vs gradD")
plt.gray()
plt.subplot(223)
plt.scatter(iterms, gradD, c=iterms)
plt.title("iterms vs gradD")
plt.gray()
plt.subplot(224)
plt.plot(iterms, gradD, 'ro')
plt.title("iterms vs gradD")

plt.show()