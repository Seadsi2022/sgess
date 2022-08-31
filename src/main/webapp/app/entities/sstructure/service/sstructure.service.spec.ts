import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISstructure } from '../sstructure.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sstructure.test-samples';

import { SstructureService } from './sstructure.service';

const requireRestSample: ISstructure = {
  ...sampleWithRequiredData,
};

describe('Sstructure Service', () => {
  let service: SstructureService;
  let httpMock: HttpTestingController;
  let expectedResult: ISstructure | ISstructure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SstructureService);
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

    it('should create a Sstructure', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sstructure = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sstructure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sstructure', () => {
      const sstructure = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sstructure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sstructure', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sstructure', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sstructure', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSstructureToCollectionIfMissing', () => {
      it('should add a Sstructure to an empty array', () => {
        const sstructure: ISstructure = sampleWithRequiredData;
        expectedResult = service.addSstructureToCollectionIfMissing([], sstructure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sstructure);
      });

      it('should not add a Sstructure to an array that contains it', () => {
        const sstructure: ISstructure = sampleWithRequiredData;
        const sstructureCollection: ISstructure[] = [
          {
            ...sstructure,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSstructureToCollectionIfMissing(sstructureCollection, sstructure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sstructure to an array that doesn't contain it", () => {
        const sstructure: ISstructure = sampleWithRequiredData;
        const sstructureCollection: ISstructure[] = [sampleWithPartialData];
        expectedResult = service.addSstructureToCollectionIfMissing(sstructureCollection, sstructure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sstructure);
      });

      it('should add only unique Sstructure to an array', () => {
        const sstructureArray: ISstructure[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sstructureCollection: ISstructure[] = [sampleWithRequiredData];
        expectedResult = service.addSstructureToCollectionIfMissing(sstructureCollection, ...sstructureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sstructure: ISstructure = sampleWithRequiredData;
        const sstructure2: ISstructure = sampleWithPartialData;
        expectedResult = service.addSstructureToCollectionIfMissing([], sstructure, sstructure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sstructure);
        expect(expectedResult).toContain(sstructure2);
      });

      it('should accept null and undefined values', () => {
        const sstructure: ISstructure = sampleWithRequiredData;
        expectedResult = service.addSstructureToCollectionIfMissing([], null, sstructure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sstructure);
      });

      it('should return initial array if no Sstructure is added', () => {
        const sstructureCollection: ISstructure[] = [sampleWithRequiredData];
        expectedResult = service.addSstructureToCollectionIfMissing(sstructureCollection, undefined, null);
        expect(expectedResult).toEqual(sstructureCollection);
      });
    });

    describe('compareSstructure', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSstructure(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSstructure(entity1, entity2);
        const compareResult2 = service.compareSstructure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSstructure(entity1, entity2);
        const compareResult2 = service.compareSstructure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSstructure(entity1, entity2);
        const compareResult2 = service.compareSstructure(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
