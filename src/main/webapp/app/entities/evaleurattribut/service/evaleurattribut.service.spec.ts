import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEvaleurattribut } from '../evaleurattribut.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../evaleurattribut.test-samples';

import { EvaleurattributService } from './evaleurattribut.service';

const requireRestSample: IEvaleurattribut = {
  ...sampleWithRequiredData,
};

describe('Evaleurattribut Service', () => {
  let service: EvaleurattributService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvaleurattribut | IEvaleurattribut[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EvaleurattributService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Evaleurattribut', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const evaleurattribut = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evaleurattribut).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evaleurattribut', () => {
      const evaleurattribut = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evaleurattribut).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evaleurattribut', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evaleurattribut', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Evaleurattribut', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEvaleurattributToCollectionIfMissing', () => {
      it('should add a Evaleurattribut to an empty array', () => {
        const evaleurattribut: IEvaleurattribut = sampleWithRequiredData;
        expectedResult = service.addEvaleurattributToCollectionIfMissing([], evaleurattribut);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaleurattribut);
      });

      it('should not add a Evaleurattribut to an array that contains it', () => {
        const evaleurattribut: IEvaleurattribut = sampleWithRequiredData;
        const evaleurattributCollection: IEvaleurattribut[] = [
          {
            ...evaleurattribut,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvaleurattributToCollectionIfMissing(evaleurattributCollection, evaleurattribut);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evaleurattribut to an array that doesn't contain it", () => {
        const evaleurattribut: IEvaleurattribut = sampleWithRequiredData;
        const evaleurattributCollection: IEvaleurattribut[] = [sampleWithPartialData];
        expectedResult = service.addEvaleurattributToCollectionIfMissing(evaleurattributCollection, evaleurattribut);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaleurattribut);
      });

      it('should add only unique Evaleurattribut to an array', () => {
        const evaleurattributArray: IEvaleurattribut[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evaleurattributCollection: IEvaleurattribut[] = [sampleWithRequiredData];
        expectedResult = service.addEvaleurattributToCollectionIfMissing(evaleurattributCollection, ...evaleurattributArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evaleurattribut: IEvaleurattribut = sampleWithRequiredData;
        const evaleurattribut2: IEvaleurattribut = sampleWithPartialData;
        expectedResult = service.addEvaleurattributToCollectionIfMissing([], evaleurattribut, evaleurattribut2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaleurattribut);
        expect(expectedResult).toContain(evaleurattribut2);
      });

      it('should accept null and undefined values', () => {
        const evaleurattribut: IEvaleurattribut = sampleWithRequiredData;
        expectedResult = service.addEvaleurattributToCollectionIfMissing([], null, evaleurattribut, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaleurattribut);
      });

      it('should return initial array if no Evaleurattribut is added', () => {
        const evaleurattributCollection: IEvaleurattribut[] = [sampleWithRequiredData];
        expectedResult = service.addEvaleurattributToCollectionIfMissing(evaleurattributCollection, undefined, null);
        expect(expectedResult).toEqual(evaleurattributCollection);
      });
    });

    describe('compareEvaleurattribut', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvaleurattribut(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEvaleurattribut(entity1, entity2);
        const compareResult2 = service.compareEvaleurattribut(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEvaleurattribut(entity1, entity2);
        const compareResult2 = service.compareEvaleurattribut(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEvaleurattribut(entity1, entity2);
        const compareResult2 = service.compareEvaleurattribut(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
