# -*- encoding: utf-8 -*-
import random
from Life import Life

class GA(object):
      """遗传算法类"""
      def __init__(self, aOrder,aCosts,aAlpha,aCrossRate, aMutationRage, aLifeCount, aGeneLenght, aMatchFun = lambda life : 1):
            self.init = aOrder
            self.costs = aCosts
            self.alpha = aAlpha
            self.croessRate = aCrossRate
            self.mutationRate = aMutationRage
            self.lifeCount = aLifeCount
            self.geneLenght = aGeneLenght
            self.matchFun = aMatchFun                 # 适配函数
            self.lives = []                           # 种群
            self.best = Life(aOrder)                         # 保存这一代中最好的个体
            self.initScore = self.matchFun(self.best)
            self.best.score = self.initScore
            self.generation = 1
            self.crossCount = 0
            self.mutationCount = 0
            self.bounds = 0.0                         # 适配值之和，用于选择是计算概率

            self.initPopulation()

      def randomDFS(self,root,tree):
          order = randomSort(self.costs[root])
          leaves = []
          for i in order:
              if tree[i[0]][0] == -1:
                if (i[1] + tree[root][1]) < (self.costs[i[0]][0] * self.alpha): #feasible
                    tree[i[0]][0] = root
                    tree[i[0]][1] = i[1] + tree[root][1]
                    self.randomDFS(i[0],tree)
                else :
                    leaves.append(i)
          return tree
      def initPopulation(self):
            """初始化种群"""
            self.lives = []
            for i in range(self.lifeCount):
                  tree = [[-1,10000] for x in range(self.geneLenght+1)]
                  tree[0] = [0,0]
                  tree = self.randomDFS(0,tree)
                  tree = tree[1:]
                  gene = []
                  for j in tree:
                      gene.append(j[0])
                  #gene = [ random.randint(0,self.geneLenght) for x in range(self.geneLenght) ]
                  #random.shuffle(gene)
                  life = Life(gene)
                  self.lives.append(life)

      def judge(self):
            """评估，计算每一个个体的适配值"""
            self.bounds = 0.0
            #self.best = self.lives[0]
            for life in self.lives:
                  life.score = self.matchFun(life)
                  self.bounds += life.score
                  if self.best.score < life.score:
                        self.best = life


      def cross(self, parent1, parent2):
            """corss over"""
            newGene = []
            index1 = random.randint(0, self.geneLenght - 1)#找一个节点
            length = random.randint(0,self.geneLenght)#交叉基因长度
            newGene = [-1 for x in range(self.geneLenght)]
            newGene = iterator(index1,newGene,parent2,length)   # 交叉的基因片段
            
            for i in newGene:
                if i == -1:
                    newGene[newGene.index(i)] = parent1.gene[newGene.index(i)]
            self.crossCount += 1
            return newGene


      def mutation1(self,gene):
            newGene = gene[:]# 产生一个新的基因序列，以免变异的时候影响父种群
            if compareGene(newGene,self.best.gene):
                leaves = findLeaves(newGene,self.geneLenght)
                if len(leaves)>0:
                    index1 = random.randint(0, len(leaves)-1)#找一个叶子
                    distLeaves = []#这个叶子和其他叶子的距离
                    for leaf in range(len(leaves)):
                        distance = self.costs[leaves[index1]][leaves[leaf]]
                        distLeaves.append(distance)
                    order = distSort(distLeaves)
                    index2 = order[0][0]#找最近的叶子
                    newGene[leaves[index1]-1] = leaves[index2]#把两个叶子连起来
            else:
                for i in range(3):
                    index1 = random.randint(0, self.geneLenght - 1)
                    index2 = random.randint(0, self.geneLenght)
                    newGene = gene[:]
                    newGene[index1] = index2
            self.mutationCount += 1
            
            return newGene

      def  mutation2(self, gene):
            """突变"""
            newGene = gene[:]       # 产生一个新的基因序列，以免变异的时候影响父种群

            index1 = random.randint(0, self.geneLenght - 1)
            index2 = random.randint(0, self.geneLenght)
           
            newGene[index1] = index2
            self.mutationCount += 1
            return newGene


      def getOne(self):
            """选择一个个体"""
            r = random.uniform(0, self.bounds)
            for life in self.lives:
                  r -= life.score
                  if r <= 0:
                        return life

            raise Exception("选择错误", self.bounds)


      def newChild(self):
            """产生新后的"""
            parent1 = self.getOne()
            rate = random.random()

            # 按概率交叉
            if rate < self.croessRate:
                  # 交叉
                  parent2 = self.getOne()
                  gene = self.cross(parent1, parent2)
            else:
                  gene = parent1.gene

            # 按概率突变
            rate = random.random()
            if rate < self.mutationRate:
                  rate = random.random()
                  if rate < 0.3:
                    gene = self.mutation1(gene)
                  else:
                    gen = self.mutation2(gene)
            return Life(gene)


      def next(self):
            """产生下一代"""
            self.judge()
            newLives = []
            """for lf in self.lives:
                if lf.score > 5*self.initScore:
                    newLives.append(lf)"""
            newLives.append(self.best)            #把最好的个体加入下一代
            #basicgene = [ 0 for x in range(self.geneLenght)]#把最基本的加入下一代
            #newLives.append(Life(basicgene))
            #newLives.append(Life(self.init))      #把DFS得到的加入下一代
            
            while len(newLives) < self.lifeCount:
                  newLives.append(self.newChild())
            self.lives = newLives
            self.generation += 1


def findLeaves(Gene,nNodes):
    arcs = [-1] * (nNodes + 1)
    outDegree = [0] * (nNodes + 1)
    inDegree = [0] * (nNodes + 1)
    graph = dict()
    leaves = []
    count = 0
    for cell in Gene:
        s = count + 1
        t = Gene[count]
        graph[s] = []
        graph[s].append(t)
        if arcs[s] == -1:
            arcs[s] = t
        else:
            print "break",leaves
            return leaves
        outDegree[s] += 1
        inDegree[t] += 1
        count = count + 1
    
    for i in range(len(inDegree)):
        if inDegree[i] == 0:
            leaves.append(i)
    print "full",leaves," ",len(leaves)
    return leaves


def compareGene(Gene1,Gene2):
    for i in range(len(Gene1)):
        if Gene1[i] != Gene2[i]:
            return False
    return True

def randomSort(arr):
    b=sorted(enumerate(arr), key=lambda k: random.random())
    return b

def distSort(arr):
    b=sorted(enumerate(arr), key=lambda x:x[1] )
    return b

def iterator(index,patch,parent,length):
    if length > 0:
        patch[index] = parent.gene[index]
        length -= 1
        index = parent.gene[index] -1
        if index == -1:
            return patch
        iterator(index,patch,parent,length)
    return patch

